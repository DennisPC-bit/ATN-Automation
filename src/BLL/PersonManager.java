package BLL;

import BE.Person;
import DAL.DataInterface;
import DAL.MockDB;
import java.util.List;

/**
 * Author DennisPC-bit
 *
 */

public class PersonManager {
    DataInterface di = new MockDB();

    public List<Person> getPersonList(){
        return di.getPersonList();
    }
}
