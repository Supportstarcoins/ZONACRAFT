package launcher.client;

import launcher.LauncherAPI;
import launcher.helper.LogHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public final class LocalOfflineLauncher {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_]{3,16}");
    private static final String LAUNCHWRAPPER_CLASS = "net/minecraft/launchwrapper/Launch.class";
    private static final String MODERN_MAIN_CLASS = "net/minecraft/client/main/Main.class";
    private static final String LEGACY_MAIN_CLASS = "net/minecraft/client/Minecraft.class";
    private static final String FML_TWEAKER_CLASS = "cpw/mods/fml/common/launcher/FMLTweaker.class";

    private LocalOfflineLauncher() {}

    @LauncherAPI
    public static Process launch(Path updatesDir, String profileName, String username,
                                 boolean fullScreen, int ram) throws IOException {
        Objects.requireNonNull(updatesDir, "updatesDir");
        Objects.requireNonNull(profileName, "profileName");
        Objects.requireNonNull(username, "username");
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Offline nickname must match [A-Za-z0-9_]{3,16}");
        }
        Path normalizedUpdatesDir = updatesDir.toAbsolutePath().normalize();
        Path clientDir = resolveClientDir(normalizedUpdatesDir, profileName);
        List<Path> classPath = collectClassPath(normalizedUpdatesDir, clientDir);
        if (classPath.isEmpty()) throw new IOException("No JAR files were found in local client directory: " + clientDir);

        String mainClass;
        boolean launchWrapper = containsClass(classPath, LAUNCHWRAPPER_CLASS);
        boolean fmlTweaker = containsClass(classPath, FML_TWEAKER_CLASS);
        if (launchWrapper) mainClass = "net.minecraft.launchwrapper.Launch";
        else if (containsClass(classPath, MODERN_MAIN_CLASS)) mainClass = "net.minecraft.client.main.Main";
        else if (containsClass(classPath, LEGACY_MAIN_CLASS)) mainClass = "net.minecraft.client.Minecraft";
        else throw new IOException("Minecraft main class was not found in: " + clientDir);

        Path javaBin = resolveJavaBinary();
        Path assetsDir = firstExistingDirectory(clientDir.resolve("assets"), normalizedUpdatesDir.resolve("assets"), clientDir.resolve("resources"), normalizedUpdatesDir.resolve("resources"), clientDir);
        Path nativesDir = firstExistingDirectory(clientDir.resolve("natives"), clientDir.resolve("bin").resolve("natives"), normalizedUpdatesDir.resolve("natives"), normalizedUpdatesDir.resolve("bin").resolve("natives"));

        List<String> command = new ArrayList<>();
        command.add(javaBin.toString());
        if (ram > 0) {
            int safeRam = Math.max(512, ram);
            command.add("-Xms" + Math.min(512, safeRam) + "M");
            command.add("-Xmx" + safeRam + "M");
        }
        if (nativesDir != null) {
            command.add("-Djava.library.path=" + nativesDir);
            command.add("-Dorg.lwjgl.librarypath=" + nativesDir);
        }
        command.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
        command.add("-Dfml.ignorePatchDiscrepancies=true");
        command.add("-Dminecraft.launcher.brand=ZONACRAFT");
        command.add("-Dminecraft.launcher.version=offline");
        command.add("-classpath");
        command.add(joinClassPath(classPath));
        command.add(mainClass);
        if ("net.minecraft.client.Minecraft".equals(mainClass)) {
            command.add(username);
            command.add("0");
        } else {
            command.add("--username"); command.add(username);
            command.add("--version"); command.add("1.6.4");
            command.add("--gameDir"); command.add(clientDir.toString());
            command.add("--assetsDir"); command.add(assetsDir.toString());
            if (fullScreen) { command.add("--fullscreen"); command.add("true"); }
            if (launchWrapper && fmlTweaker) { command.add("--tweakClass"); command.add("cpw.mods.fml.common.launcher.FMLTweaker"); }
        }
        LogHelper.info("Starting offline client '%s' from: %s", profileName, clientDir);
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(clientDir.toFile());
        builder.redirectErrorStream(true);
        builder.inheritIO();
        builder.environment().put("_JAVA_OPTIONS", "");
        builder.environment().put("JAVA_TOOL_OPTIONS", "");
        builder.environment().put("JAVA_OPTS", "");
        return builder.start();
    }

    private static Path resolveClientDir(Path updatesDir, String profileName) throws IOException {
        List<Path> candidates = new ArrayList<>();
        candidates.add(updatesDir.resolve(profileName));
        candidates.add(updatesDir.resolve("clients").resolve(profileName));
        candidates.add(updatesDir.resolve("client").resolve(profileName));
        if (profileName.endsWith("_shaders")) {
            String baseName = profileName.substring(0, profileName.length() - "_shaders".length());
            candidates.add(updatesDir.resolve(baseName));
            candidates.add(updatesDir.resolve("clients").resolve(baseName));
            candidates.add(updatesDir.resolve("client").resolve(baseName));
        }
        candidates.add(updatesDir.resolve("client"));
        candidates.add(updatesDir);
        for (Path candidate : candidates) if (Files.isDirectory(candidate) && containsJar(candidate)) return candidate.toAbsolutePath().normalize();
        throw new IOException("Local client profile '" + profileName + "' was not found under " + updatesDir + ". Offline mode can only start files that are already installed.");
    }

    private static boolean containsJar(Path root) throws IOException {
        final boolean[] found = {false};
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (name.endsWith(".jar") || name.endsWith(".zip")) { found[0] = true; return FileVisitResult.TERMINATE; }
                return FileVisitResult.CONTINUE;
            }
        });
        return found[0];
    }

    private static List<Path> collectClassPath(Path updatesDir, Path clientDir) throws IOException {
        final Set<Path> entries = new LinkedHashSet<>();
        entries.add(clientDir);
        addDirectoryIfPresent(entries, clientDir.resolve("bin"));
        scanJars(clientDir, entries);
        Path libraries = updatesDir.resolve("libraries");
        if (Files.isDirectory(libraries) && !libraries.startsWith(clientDir)) scanJars(libraries, entries);
        Path lib = updatesDir.resolve("lib");
        if (Files.isDirectory(lib) && !lib.startsWith(clientDir)) scanJars(lib, entries);
        List<Path> result = new ArrayList<>(entries);
        Collections.sort(result, new Comparator<Path>() {
            @Override public int compare(Path left, Path right) {
                int lp = classPathPriority(left), rp = classPathPriority(right);
                return lp != rp ? Integer.compare(lp, rp) : left.toString().compareToIgnoreCase(right.toString());
            }
        });
        return result;
    }

    private static void addDirectoryIfPresent(Set<Path> entries, Path directory) { if (Files.isDirectory(directory)) entries.add(directory.toAbsolutePath().normalize()); }
    private static void scanJars(Path root, final Set<Path> entries) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
                if (name.endsWith(".jar") || name.endsWith(".zip")) entries.add(file.toAbsolutePath().normalize());
                return FileVisitResult.CONTINUE;
            }
        });
    }
    private static int classPathPriority(Path path) {
        String name = path.getFileName() == null ? "" : path.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.contains("launchwrapper")) return 0;
        if (name.contains("forge") || name.contains("fml")) return 1;
        if (name.contains("minecraft")) return 2;
        if (Files.isDirectory(path)) return 3;
        return 4;
    }
    private static boolean containsClass(List<Path> classPath, String className) {
        for (Path entry : classPath) {
            if (Files.isDirectory(entry)) { if (Files.isRegularFile(entry.resolve(Paths.get(className)))) return true; continue; }
            try (JarFile jar = new JarFile(entry.toFile())) { if (jar.getEntry(className) != null) return true; } catch (IOException ignored) {}
        }
        return false;
    }
    private static String joinClassPath(List<Path> classPath) {
        StringBuilder result = new StringBuilder();
        for (Path entry : classPath) { if (result.length() > 0) result.append(File.pathSeparatorChar); result.append(entry); }
        return result.toString();
    }
    private static Path resolveJavaBinary() throws IOException {
        Path javaHome = Paths.get(System.getProperty("java.home"));
        String executable = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win") ? "java.exe" : "java";
        Path javaBin = javaHome.resolve("bin").resolve(executable);
        if (!Files.isRegularFile(javaBin)) throw new IOException("Java binary was not found: " + javaBin);
        return javaBin.toAbsolutePath().normalize();
    }
    private static Path firstExistingDirectory(Path... candidates) {
        for (Path candidate : candidates) if (candidate != null && Files.isDirectory(candidate)) return candidate.toAbsolutePath().normalize();
        return null;
    }
}
