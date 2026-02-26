package com.lib.screening.xml;

import com.lib.screening.DLNAManager;

import org.fourthline.cling.binding.staging.MutableService;
import org.fourthline.cling.binding.xml.DescriptorBindingException;
import org.fourthline.cling.binding.xml.UDA10ServiceDescriptorBinderSAXImpl;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Service;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class DLNAUDA10ServiceDescriptorBinderSAXImpl extends UDA10ServiceDescriptorBinderSAXImpl {
    private static final String TAG = "DLNAUDA10ServiceDescriptorBinderSAXImpl";

    @Override
    public <S extends Service> S describe(S s, String str) throws DescriptorBindingException, ValidationException {
        if (str == null || str.length() == 0) {
            throw new DescriptorBindingException("Null or empty descriptor");
        }
        try {
            String str2 = TAG;
            DLNAManager.logD(str2, "Reading service from XML descriptor, content : \n" + str);
            DLNASAXParser dLNASAXParser = new DLNASAXParser();
            MutableService mutableService = new MutableService();
            hydrateBasic(mutableService, s);
            new UDA10ServiceDescriptorBinderSAXImpl.RootHandler(mutableService, dLNASAXParser);
            dLNASAXParser.parse(new InputSource(new StringReader(str.trim())));
            return (S) mutableService.build(s.getDevice());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e2) {
            throw new DescriptorBindingException("Could not parse service descriptor: " + e2.toString(), e2);
        }
    }
}
