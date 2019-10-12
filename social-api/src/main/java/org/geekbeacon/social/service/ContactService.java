package org.geekbeacon.social.service;

import org.geekbeacon.social.model.Contact;

import java.util.List;

public interface ContactService {

    List<Contact> getContactListing(String domain);
}
