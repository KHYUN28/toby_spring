public class LegacyUserAdapter implements UserAdapter {

  private LegacyUserService legacyUserService;

  public LegacyUserAdapter(LegacyUserService legacyUserService) {
    this.legacyUserService = legacyUserService;
  }

  @Override
  public String getFullName(int userId) {
    return legacyUserService.fetchUserName(userId);
  }
}
