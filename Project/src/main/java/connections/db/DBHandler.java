package connections.db;

import java.sql.*;
import java.time.LocalDateTime;

import connections.IDAssigner;
import stream.Category;
import stream.LiveStream;
import user.Status;
import user.Streamer;
import user.User;

public class DBHandler {
    private Connection connection;
    private IDAssigner assigner;

    public DBHandler(IDAssigner assigner) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String password = "Bananasql1!";
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/livestreamdb?characterEncoding=latin1&useConfigs=maxPerformance","root",password);
            this.assigner = assigner;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String[] browseCategory(Category cat) {
        String[] ans = null;
        try {
            int category = cat.geti();
            String query = "Select * from streams where category=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, category);
            ResultSet rs = ps.executeQuery();


            int size = getSize(rs);
            if(size==0) { return null; }

            ans = new String[size];
            int index = 0;
            while(rs.next()) {
                ans[index++] = rs.getString("streamerusername");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public synchronized String[] getNotifications(String selfUsername) {
        String[] ans = null;
        try {
            String query = "Select * from follows where followerusername=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, selfUsername);
            ResultSet rs = ps.executeQuery();


            int size = getSize(rs);
            if(size==0) { return null; }

            ans = new String[size];
            int index = 0;
            while(rs.next()) {
                //Check if followed streamer is live or not
                String subquery = "Select * from streams where streamerusername=?";
                PreparedStatement pss = connection.prepareStatement(subquery);
                pss.setString(1, rs.getString("streamerusername"));
                ResultSet rss = pss.executeQuery();

                if(rss.next()) { ans[index++] = rss.getString("streamerusername"); }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public synchronized User getUserByUsername(String searchTerm) {
        try {
            String query = "Select * from Users where username=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, searchTerm);
            ResultSet rs = ps.executeQuery();
            int size = getSize(rs);
            if(size==0) { return null; }
            else {
                rs.next();
                String name = rs.getString("name");
                String bio = rs.getString("bio");
                String dpLocation = rs.getString("dplocation");
                int stat = rs.getInt("status");
                Status status = Status.OFFLINE;
                switch(stat) {
                    case 0: status = Status.OFFLINE; break;
                    case 1: status = Status.ONLINE; break;
                    case 2: status = Status.WATCHING; break;
                    case 3: status = Status.STREAMING; break;
                    default: break;
                }
                LocalDateTime lastseen = rs.getTimestamp("lastseen").toLocalDateTime();
                User target = new User(searchTerm,name,bio,status);
                target.setLastseen(lastseen);
                return target;
            }
        }
        catch(SQLException e) {
            System.out.println("Meme");
        }
        return null;
    }

    public synchronized User[] getSubList() {
        return null;
    }

    public synchronized User[] getFollowList(User self) {
        User[] followList = new User[0];
        try {
            String query = "Select * from Follows where follower=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, self.getUsername());
            ResultSet rs = ps.executeQuery();
            int size = getSize(rs);
            followList = new User[size];
            int index = 0;
            while(rs.next()) {
                String streamerUsername = rs.getString("streamer");
                User user = this.getUserByUsername(streamerUsername);
                followList[index++] = user;
            }
        }
        catch(SQLException e) {
            System.out.println("Meme");
        }
        return followList;
    }

    public synchronized void addStreamtoDB(String streamerUsername, String title, int category, java.sql.Timestamp starttime) {
        try {
            PreparedStatement pst = connection.prepareStatement("insert into streams (streamerusername, title, category, viewcount, starttime) values(?,?,?,?,?)");

            pst.setString(1,streamerUsername);
            pst.setString(2,title);
            pst.setInt(3,category);
            pst.setInt(4,0);
            pst.setTimestamp(5,starttime);

            pst.executeUpdate();

        }
        catch(Exception e) {
            e.printStackTrace();;
        }
    }

    public synchronized void removeStreamfromDB(String streamerUsername) {
        try {
            String query = "delete from streams where streamerusername=?";
            PreparedStatement pst = connection.prepareStatement(query);

            pst.setString(1,streamerUsername);

            pst.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized LiveStream getStream(String streamerUsername) {
        try {
            String query = "select * from streams where streamerusername=?";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1,streamerUsername);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                int id = assigner.getID(streamerUsername);
                String title = rs.getString("title");
                Category cat = null; int c = rs.getInt("category");
                for(Category i : Category.values()) {
                    if(i.compare(c)) { cat = i; break; }
                }
                LocalDateTime starttime = rs.getTimestamp("starttime").toLocalDateTime();

                LiveStream livestream = new LiveStream(title, cat, id, streamerUsername);
                livestream.setStartedAtTime(starttime);
                return livestream;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized  void setStatus(String selfUsername, Status status) {
        try {
            String update = "update users set status=? where username=?";
            PreparedStatement pst = connection.prepareStatement(update);

            pst.setInt(1, status.geti());
            pst.setString(2, selfUsername);
            pst.executeUpdate();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void setLastSeen(String selfUsername, java.sql.Timestamp now) {
        try {
            String update = "update users set lastseen=? where username=?";
            PreparedStatement pst = connection.prepareStatement(update);

            pst.setTimestamp(1, now);
            pst.setString(2, selfUsername);
            pst.executeUpdate();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateUserInfo(String selfUsername, String newName, String newBio) {
        try {
            String update = "update users set name=?, bio=? where username=?";
            PreparedStatement pst = connection.prepareStatement(update);

            pst.setString(1, newName);
            pst.setString(2,newBio);
            pst.setString(3, selfUsername);

            pst.executeUpdate();
            System.out.println("info updated");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addFollow(String followerUsername, String streamerUsername, int bit) {
        try {
            if(bit == 1) { //Follow demand
                String query = "select * from follows where followerusername=? and streamerusername=?";
                PreparedStatement pst = connection.prepareStatement(query);
                pst.setString(1, followerUsername);
                pst.setString(2, streamerUsername);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) return; //If pair already exists dont have to do anything

                String update = "insert into follows (followerusername, streamerusername) values(?,?)";
                pst = connection.prepareStatement(update);
                pst.setString(1, followerUsername);
                pst.setString(2, streamerUsername);

                pst.executeUpdate();
            }
            else { //Unfollow demand
                String query = "delete from follows where followerusername=? and streamerusername=?";
                PreparedStatement pst = connection.prepareStatement(query);

                pst.setString(1, followerUsername);
                pst.setString(2, streamerUsername);

                pst.execute();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addSub(String subberUsername, String streamerUsername, int bit, java.sql.Timestamp now) {
        try {
            if(bit == 1) { //Sub demand
                String query = "select * from subs where subberusername=? and streamerusername=?";
                PreparedStatement pst = connection.prepareStatement(query);
                pst.setString(1, subberUsername);
                pst.setString(2, streamerUsername);

                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String update = "update subs set lastsubbed=? where subberusername=? and streamerusername=?";
                    pst = connection.prepareStatement(update);
                    pst.setTimestamp(1,now);
                    pst.setString(2, subberUsername);
                    pst.setString(3, streamerUsername);
                } //If pair already exists then just update lastsubbed

                else {
                    String update = "insert into subs (subberusername, streamerusername, firstsubbed, lastsubbed) values(?,?,?,?)";
                    pst = connection.prepareStatement(update);
                    pst.setString(1, subberUsername);
                    pst.setString(2, streamerUsername);
                    pst.setTimestamp(3, now);
                    pst.setTimestamp(4, now);

                    pst.executeUpdate();
                }
                //Remove 5 bucks from viewer

                updateBalance(subberUsername, -5);

            }
            else { //Unsub demand
                String query = "delete from subs where subberusername=? and streamerusername=?";
                PreparedStatement pst = connection.prepareStatement(query);

                pst.setString(1, subberUsername);
                pst.setString(2, streamerUsername);

                pst.execute();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized int getBalance(String username) {
        try {
            String query = "select * from users where username=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            rs.next(); int bal = rs.getInt("balance");

            return bal;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public synchronized void updateBalance(String username, int increment) {
        try {
            int bal = getBalance(username);

            String update = "update users set balance=? where username=?";
            PreparedStatement pst = connection.prepareStatement(update);
            pst.setInt(1, bal + increment);
            pst.setString(2, username);

            pst.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized int[] getUserPairInfo(String user1, String user2) {
        int[] ans = new int[3];  //0 stores followed, 1 stores subbed
        try {

            String query = "select * from follows where followerusername=? and streamerusername=?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1,user1);
            pst.setString(2,user2);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) ans[0] = 1; else ans[0] = 0;

            query = "select * from subs where subberusername=? and streamerusername=?";
            pst = connection.prepareStatement(query);
            pst.setString(1,user1);
            pst.setString(2,user2);
            rs = pst.executeQuery();

            if(rs.next()) ans[1] = 1; else ans[1] = 0;

            ans[2] = getBalance(user1);

            return ans;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    static int getSize(ResultSet rs) {
        int size = 0;
        try {
            if (rs != null) {
                rs.last();
                size = rs.getRow();
                rs.beforeFirst();
            }
        } catch (Exception e) {
        } 
        return size;
    }
}
