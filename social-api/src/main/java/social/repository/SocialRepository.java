package social.repository;

import org.geekbeacon.social.db.models.config.tables.records.SocialAppRecord;
import org.geekbeacon.social.db.models.config.tables.records.UserSocialRecord;
import social.model.SocialType;

public interface SocialRepository {

    SocialAppRecord getSocialAppRecord(SocialType type);

    UserSocialRecord getUserSocialRecord(long appId);

    void saveUserSocialTokens(SocialType socialType, String token, String secret);
}
