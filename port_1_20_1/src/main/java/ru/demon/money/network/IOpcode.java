package ru.demon.money.network;

public interface IOpcode {
   DebugGroup getGroup();

   DebugPriority getPriority();

   int getOrdinal();

   String getName();
}
