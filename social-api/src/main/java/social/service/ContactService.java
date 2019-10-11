package social.service;

import social.model.Contact;

import java.util.List;

public interface ContactService {

    List<Contact> getContactListing(String domain);
}
