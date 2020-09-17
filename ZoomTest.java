import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class ZoomTest {
   static class User {
        int id;
        int meetingNo;

        public User(int id, int meetingNo) {
            this.id = id;
            this.meetingNo = meetingNo;
        }

        public int getId() {
            return id;
        }

       public int getMeetingNo() {
           return meetingNo;
       }

       public void setMeetingNo(int meetingNo) {
           this.meetingNo = meetingNo;
       }

       @Override
       public String toString() {
           return "User{" +
                   "id=" + id +
                   ", meetingNo=" + meetingNo +
                   '}';
       }
   }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User(1, 2));
        users.add(new User(1, 1));
        users.add(new User(2, 3));
        users.add(new User(2, 6));
        users.add(new User(3, 4));
        users.add(new User(3, 5));
        System.out.println(users.stream()
                .collect(groupingBy(User::getId, mapping(User::getMeetingNo, toList()))).toString());
    }
}
