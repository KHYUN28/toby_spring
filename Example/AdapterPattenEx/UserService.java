@Service
public class UserService {

  private UserAdapter userAdapter;

  public UserService(UserAdapter userAdapter) {
    this.userAdapter = userAdapter;
  }

  public String getFullName(int userId) {
    return userAdapter.getFullName(userId);
  }
}