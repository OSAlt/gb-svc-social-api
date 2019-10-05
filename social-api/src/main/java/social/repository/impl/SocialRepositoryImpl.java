package social.repository.impl;

import lombok.NonNull;
import org.geekbeacon.social.db.models.config.tables.UserSocial;
import org.geekbeacon.social.db.models.config.tables.records.SocialAppRecord;
import org.geekbeacon.social.db.models.config.tables.records.UserSocialRecord;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import social.model.SocialType;
import social.repository.SocialRepository;

import static org.geekbeacon.social.db.models.config.Config.CONFIG;

@Repository
@CacheConfig(cacheNames = {"socialData"})
public class SocialRepositoryImpl implements SocialRepository {

    private final DSLContext dslContext;

    public SocialRepositoryImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * @see SocialRepository#getSocialAppRecord(SocialType)
     */
    @Cacheable
    @Override
    public SocialAppRecord getSocialAppRecord(@NonNull SocialType type) {
        SocialAppRecord record =
            dslContext.select(
                CONFIG.SOCIAL_APP.APP_ID,
                CONFIG.SOCIAL_APP.CLIENT_ID,
                CONFIG.SOCIAL_APP.SECRET,
                CONFIG.SOCIAL_APP.CALLBACK_URL)
                .from(CONFIG.SOCIAL_APP)
                .where(CONFIG.SOCIAL_APP.SOCIAL_APP_TYPE.eq(type.name()))
                .limit(1)
                .fetchOneInto(SocialAppRecord.class);
        return record;
    }

    @Cacheable
    @Override
    public UserSocialRecord getUserSocialRecord(long appId) {
            UserSocialRecord record =
            dslContext.selectFrom(CONFIG.USER_SOCIAL)
                .where(CONFIG.USER_SOCIAL.APP_ID.eq(appId))
                .limit(1)
                .fetchOneInto(UserSocialRecord.class);

            return record;
    }

    /**
     * @see SocialRepository#saveUserSocialTokens(SocialType, String, String)
     */
    @Override
    public void saveUserSocialTokens(SocialType socialType, String token, String secret) {
        SocialAppRecord socialAppRecord = getSocialAppRecord(socialType);
        dslContext.insertInto(CONFIG.USER_SOCIAL)
            .set(CONFIG.USER_SOCIAL.ACCESS_TOKEN, token)
            .set(CONFIG.USER_SOCIAL.SECRET_TOKEN, secret)
            .set(CONFIG.USER_SOCIAL.USER_ID, "")
            .set(CONFIG.USER_SOCIAL.APP_ID, socialAppRecord.getAppId())
            .execute();
    }
}
