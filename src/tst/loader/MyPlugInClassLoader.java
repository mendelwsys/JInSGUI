package tst.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 16.05.17
 * Time: 17:57
 * Extend default Permissions
 */
public class MyPlugInClassLoader extends URLClassLoader {
    public MyPlugInClassLoader(URL[] classLoaderUrls, ClassLoader parentLoader) {
        super(classLoaderUrls, parentLoader);
    }

    protected PermissionCollection getPermissions(CodeSource codesource)
    {
        System.out.println("!!!CALL PERMITIONS!!!");
        PermissionCollection rv = super.getPermissions(codesource);
        rv.add(new AllPermission());
        System.out.println("!!!CALL PERMITIONS END!!!");
        return rv;
    }
}
