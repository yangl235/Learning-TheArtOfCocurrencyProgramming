package special.webservice.mockito;


import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MockitoTest {
    @Test
    public void testMockList() {
        //mock creation
        List mockedList = mock(List.class);

        //using mock object
        mockedList.add("one");
        mockedList.clear();

        //verification
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

    @Test
    public void testMockLinkedList() {
        //You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        mockedList.set(0, "ok");
        System.out.println(mockedList.get(0));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        //verify(mockedList).get(0);
    }

    @Test
    public void testArgument() {
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");

        //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
        //when(mockedList.contains(argThat(isValid()))).thenReturn("element");

        //following prints "element"
        System.out.println(mockedList.get(999));

        //you can also verify using an argument matcher
        verify(mockedList).get(anyInt());


    }

}
