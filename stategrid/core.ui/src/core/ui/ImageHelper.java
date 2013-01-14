package core.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;

public class ImageHelper {
	
	public static Image LoadImage(String pluginId,String imgPath){
		Bundle bundle = Platform.getBundle(pluginId);
        if (!BundleUtility.isReady(bundle)) {
			return null;
		}
        
        URL fullPathString = BundleUtility.find(bundle, imgPath);
        
        if (fullPathString == null) {
            try {
                fullPathString = new URL(imgPath);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        return ImageDescriptor.createFromURL(fullPathString).createImage();
	}
	
	public static Image getImage(String strCategoryAndName){
		return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(strCategoryAndName));
	}
	
	public static Image getImage(String strCategoryAndName,int w,int h){
		return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(strCategoryAndName),w,h);
	}
	
}
