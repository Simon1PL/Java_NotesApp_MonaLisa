package pl.edu.agh.monalisa.controller;

import org.junit.jupiter.api.Test;
import pl.edu.agh.monalisa.model.AssignmentFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class HistoryTest {

    @Test
    public void whenEmptyRedoAndUndoShouldBeDisabled() {
        //given
        History history = new History();

        //then
        assertTrue(history.isRedoDisabled().getValue());
        assertTrue(history.isUndoDisabled().getValue());
    }

    @Test
    public void undoTest() {
        //given
        History history = new History();
        AssignmentFile assignmentFile = mock(AssignmentFile.class);
        AssignmentFile assignmentFile2 = mock(AssignmentFile.class);

        //when
        history.add(assignmentFile);
        history.add(assignmentFile2);

        //then
        assertTrue(history.isRedoDisabled().getValue());
        assertEquals(history.undo(), assignmentFile);
        assertFalse(history.isRedoDisabled().getValue());
    }

    @Test
    public void redoTest() {
        //given
        History history = new History();
        AssignmentFile assignmentFile = mock(AssignmentFile.class);
        AssignmentFile assignmentFile2 = mock(AssignmentFile.class);

        //when
        history.add(assignmentFile);
        history.add(assignmentFile2);
        history.undo();

        //then
        assertTrue(history.isUndoDisabled().getValue());
        assertEquals(history.redo(), assignmentFile2);
    }

    @Test
    public void theSameFileAsCurrentShouldNotBeAdded() {
        //given
        History history = new History();
        AssignmentFile assignmentFile = mock(AssignmentFile.class);

        //when
        history.add(assignmentFile);
        history.add(assignmentFile);

        //then
        assertTrue(history.isUndoDisabled().getValue());
        assertTrue(history.isRedoDisabled().getValue());
    }
}
