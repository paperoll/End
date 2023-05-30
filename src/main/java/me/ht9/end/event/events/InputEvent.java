package me.ht9.end.event.events;

import me.ht9.end.event.Event;

public class InputEvent extends Event
{
    public static class MouseInputEvent extends InputEvent {}
    public static class KeyInputEvent extends InputEvent {}
}