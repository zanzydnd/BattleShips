package mvc;

import mvc.Panels.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BotTest {

    @Test
    void botPlacingShips(){
        View view = mock(View.class);
        Board fake = mock(Board.class);
        Model model = new Model();
        view.model = model;
        Bot bot = new Bot(fake,view);
        assertTrue(bot.botPlacingShips());
    }

}