package TurnQuest.view.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/**
 *
 * @author java
 */
public class MyADSearch {

    public static void main(String[] args) {

        //String username = "joseph@turnkeyafrica.com";
        // String password = "password";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        //env.put(Context.PROVIDER_URL, "ldap://10.176.18.56");
        // env.put(Context.PROVIDER_URL,
        // "ldap://10.176.18.56");
        env.put(Context.PROVIDER_URL, "ldap://turnkeyAfrica.net");
        //env.put(Context.SECURITY_AUTHENTICATION, "simple");
        //env.put(Context.SECURITY_PRINCIPAL, username);
        //env.put(Context.SECURITY_CREDENTIALS, password);

        DirContext ctx = null;
        NamingEnumeration results = null;
        try {
            //ctx = new InitialDirContext(env);
            ctx =
ActiveDirectory.getConnection("java@turnkeyAfrica.net", "test@4ever",
                              "turnkeyAfrica.net");
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results =
                    ctx.search("DC=turnkeyAfrica,DC=net", "(objectclass=user)",
                               controls);
            while (results.hasMore()) {

                SearchResult searchResult = (SearchResult)results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute attr = attributes.get("cn");
                String cn = (String)attr.get();

                for (NamingEnumeration ae = attributes.getAll(); ae.hasMore();
                ) {
                    Attribute attrw = (Attribute)ae.next();
                    System.out.println("attribute: " + attrw.getID());
                    for (NamingEnumeration e = attrw.getAll(); e.hasMore();
                         System.out.println("value: " + e.next()))
                        ;
                }

                /*System.out.println(" Person Common Name = " + attributes.get("cn"));
                 System.out.println(" Person Display Name = " + attributes.get("displayName"));
                 System.out.println(" Person logonhours = " + attributes.get("logonhours"));
                 System.out.println(" Person MemberOf = " + attributes.get("memberOf"));
                 System.out.println(" Given Name = " + attributes.get("givenName"));
                 System.out.println(" sn = " + attributes.get("sn"));
                 System.out.println(" Principal Name = " + attributes.get("userPrincipalName"));*/
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }

    }
}
