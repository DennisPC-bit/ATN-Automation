package DAL;

import BE.Person;
import javafx.scene.image.Image;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Author DennisPC-bit
 *
 */

public class MockDB implements DataInterface {
    Random r = new Random(1337);

    @Override
    public List getPersonList() {
        var tmp = Arrays.asList(new Person(1, "Dennis", new Image("GUI/IMG/prototype i gues.gif")),new Person(2, "Carlo"),new Person(3, "Anna")
        , new Person(4, "Niko"));
        for(int i = 0 ; i < 1337 ; i++)
            tmp.get(r.nextInt(4)).attend(LocalDateTime.of(2000+r.nextInt(21),r.nextInt(12)+1,r.nextInt(28)+1,r.nextInt(24), r.nextInt(60)));
        return tmp;
    }
}
