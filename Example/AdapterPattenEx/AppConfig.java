@Configuration
public class AppConfig {

  @Bean
  public LegacyUserService legacyUserService() {
    return new LegacyUserServiceImpl();
  }

  @Bean
  public UserAdapter userAdapter(LegacyUserService legacyUserService) {
    return new LegacyUserAdapter(legacyUserService);
  }
}
