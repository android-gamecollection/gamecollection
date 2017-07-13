package todo.gamecollection.model.util;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import todo.gamecollection.model.games.chess.MoveTranslator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MoveTranslatorTest {

    @Test
    public void colToChar_correctArguments_correctResults() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("colToChar", int.class);
        method.setAccessible(true);

        String expected = "abcdefgh";
        for (int i = 0; i < 8; i++) {
            char result = (char) method.invoke(null, i);
            assertThat(result, is(expected.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void colToChar_wrongArguments_throwsException() throws Throwable {
        Method method = MoveTranslator.class.getDeclaredMethod("colToChar", int.class);
        method.setAccessible(true);

        try {
            method.invoke(null, 8);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void rowToChar_correctArguments_correctResults() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("rowToChar", int.class);
        method.setAccessible(true);

        String expected = "87654321";
        for (int i = 0; i < 8; i++) {
            char result = (char) method.invoke(null, i);
            assertThat(result, is(expected.charAt(i)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void rowToChar_wrongArguments_throwsException() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("rowToChar", int.class);
        method.setAccessible(true);

        try {
            method.invoke(null, 8);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void charToCol_correctArguments_correctResults() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("charToCol", char.class);
        method.setAccessible(true);

        for (char c = 'a'; c <= 'h'; c++) {
            int result = (int) method.invoke(null, c);
            int resultUppercase = (int) method.invoke(null, Character.toUpperCase(c));
            assertThat(result, is(c - 'a'));
            assertThat(resultUppercase, is(c - 'a'));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void charToCol_wrongArguments_throwsException() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("charToCol", char.class);
        method.setAccessible(true);

        try {
            method.invoke(null, '3');
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void charToRow_correctArguments_correctResults() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("charToRow", char.class);
        method.setAccessible(true);

        int[] expected = {7, 6, 5, 4, 3, 2, 1, 0};
        for (char c = '1'; c <= '8'; c++) {
            int result = (int) method.invoke(null, c);
            assertThat(result, is(expected[c - '1']));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void charToRow_wrongArguments_throwsException() throws Exception {
        Method method = MoveTranslator.class.getDeclaredMethod("charToRow", char.class);
        method.setAccessible(true);

        try {
            method.invoke(null, '0');
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }
}
