package org.geekbeacon.social.service.impl;

import static org.geekbeacon.social.db.models.nixie.Nixie.NIXIE;

import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.geekbeacon.social.model.Contact;
import org.geekbeacon.social.service.ContactService;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private static final String DEFAULT_DOMAIN = "nixiepixel.com";
    private final DSLContext dslContext;

    @Autowired
    public ContactServiceImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * @see ContactService#getContactListing(String)
     */
    @Override
    public List<Contact> getContactListing(String domain) {
        if(StringUtils.isEmpty(domain)) {
            domain = DEFAULT_DOMAIN;
        }
        return dslContext.select(NIXIE.CONTACT_FORM.EMAIL, NIXIE.CONTACT_FORM.DESCRIPTION)
            .from(NIXIE.CONTACT_FORM)
            .where(NIXIE.CONTACT_FORM.DOMAIN.eq(domain))
            .fetchInto(Contact.class);
    }
}
