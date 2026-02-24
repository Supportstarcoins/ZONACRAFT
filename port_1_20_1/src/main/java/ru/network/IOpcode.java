package ru.stalcraft.network;

public interface IOpcode {
   DebugGroup getGroup();

   DebugPriority getPriority();

   int getOrdinal();

   String getName();
}
