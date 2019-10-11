package social.service.impl;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import social.model.Contact;
import social.service.ContactService;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private static final String DOMAIN = "nixiepixel.com";
    private static final String EMAIL = "niki@nixiepixel.com";
    private static final List<String> categories = Lists.newArrayList(
        "General",
        "Question",
        "Comment",
        "Business",
        "Sponsorship",
        "Collaborations",
        "Advertising");

    /**
     * @see ContactService#getContactListing(String)
     */
    @Override
    public List<Contact> getContactListing(String domain) {

        List<Contact> contacts = Lists.newArrayList();
        
        for (String category : categories) {
            Contact contact = Contact.builder()
                .description(category)
                .email(EMAIL)
                .build();
            contacts.add(contact);

        }
        return contacts;
    }
}
