package tst.interprocess;

import tst.ICommonGuiStub;
import tst.IGuiStubFactory;
import tst.ILoaderCtrl;
import tst.URLCtrlImpl;
import tst.loader.MyPlugInClassLoader;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: NEMO
 * Date: 19.02.18
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
public class InitGuiFactory implements IGuiStubFactory
{

            public ICommonGuiStub createDocEditComponent(Component inParent, Map<String,String> params) throws Exception
            {
                    System.out.println("use InitGuiFactory");
                    Container parent = (Container)inParent;
                try {
                    if (parent != null)
                    {
                        ICommonGuiStub rv;

//                URL[] classLoaderUrls = new URL[]{new URL("file:///C:\\PapaWK\\Projects\\JavaProj\\appletWK\\out\\artifacts\\appletLib_jar\\appletLib.jar")};
//                ClassLoader parentLoader = getClass().getClassLoader();
//                //URLClassLoader myLoader = new URLClassLoader(classLoaderUrls, parentLoader);
//                URLClassLoader myLoader = new MyPlugInClassLoader(classLoaderUrls, parentLoader);

                        ClassLoader myLoader;
                        {
                            final URL[] classLoaderUrls;// = new URL[]{new URL("file:///C:\\PapaWK\\Projects\\JavaProj\\appletWK\\out\\artifacts\\appletLib_jar\\appletLib.jar")};
                            java.util.List<URL> lurls = this.getListUrls();
                            if (lurls!=null)
                                classLoaderUrls = lurls.toArray(new URL[lurls.size()]);
                            else
                                classLoaderUrls = new URL[0];
                            myLoader = getClass().getClassLoader();
                            if (classLoaderUrls.length!=0)
                                myLoader = new MyPlugInClassLoader(classLoaderUrls, myLoader);
                        }


                        Class beanClass=null;
                        if (params!=null && ICommonGuiStub.MODE_JGRID.equalsIgnoreCase(params.get(ICommonGuiStub.MODE_VNM)))
                        {
                            beanClass=myLoader.loadClass("com.mwlib.jalv.tbl.ServerJGridBean01");
                        }

                        if (beanClass!=null)
                        {
                            System.out.println("beanClass = " + beanClass.getCanonicalName());

                            Constructor<?> constructor = beanClass.getConstructor();
                            if (constructor!=null)
                            {
                                Component cmp = (Component) constructor.newInstance();
                                parent.add(cmp);
//                                parent.addFocusListener(new FocusListener() {
//                                    @Override
//                                    public void focusGained(FocusEvent e) {
//                                        System.out.println("parent focusGained = " );
//                                    }
//
//                                    @Override
//                                    public void focusLost(FocusEvent e) {
//                                        System.out.println("parent focusLost = " );
//
//                                    }
//                                });

                                rv=(ICommonGuiStub)cmp;
                                System.out.println("parent component = " + parent);
                                return rv;
                            }
                            System.out.println("constructor of bean class is null ") ;
                        }
                        System.out.println("beanClass is null ") ;
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                System.out.println("parent component = " + parent);
                System.out.println("!return null pointer on stub due error!");
                return null;
            }

            protected ILoaderCtrl urlCtrl = new URLCtrlImpl();
            public int addUrlString(String urlString) {
                return urlCtrl.addUrlString(urlString);
            }

            public int rmUrlString(String urlString) {
                return urlCtrl.rmUrlString(urlString);
            }

            public String getXMLUrls() {
                return urlCtrl.getXMLUrls();
            }

            public java.util.List<URL> getListUrls() {
                return urlCtrl.getListUrls();
            }
        }
