package TurnQuest.view.Connect;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;


public class LDAPConnector {
    public LDAPConnector() {
        super();
    }

    public String userAuthenticate(String username, String password) {
        String success = "failure";

        String base = "ou=people,dc=turnkeyafrica,dc=com";
        String dn = "uid=" + username + "," + base;
        String ldapURL = "ldap://10.176.18.56:389";

        try {
            // Setup environment for authenticating

            Hashtable<String, String> environment =
                new Hashtable<String, String>();
            environment.put(Context.INITIAL_CONTEXT_FACTORY,
                            "com.sun.jndi.ldap.LdapCtxFactory");
            environment.put(Context.PROVIDER_URL, ldapURL);
            environment.put(Context.SECURITY_AUTHENTICATION, "simple");
            environment.put(Context.SECURITY_PRINCIPAL, dn);
            environment.put(Context.SECURITY_CREDENTIALS, password);

            DirContext authContext = new InitialDirContext(environment);
            success = "success";
            authContext.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }
}
