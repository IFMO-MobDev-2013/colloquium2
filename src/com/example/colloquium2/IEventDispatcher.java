package com.example.colloquium2;

public interface IEventDispatcher
{
    void addEventListener(IEventHadler listener);
    void removeEventListener(IEventHadler listener);
    void dispatchEvent(Event e);
}
