package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.tasks;

import android.os.AsyncTask;
import com.jaku.core.JakuRequest;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.model.Device;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.roku.utils.RokuRequestTypes;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class RequestTask extends AsyncTask<RokuRequestTypes, Void, RequestTask.Result> {
    private RequestCallback mCallback;
    private JakuRequest request;
    private RokuRequestTypes rokuRequestType;

    /* access modifiers changed from: protected */
    public void onCancelled(Result result) {
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
    }

    public RequestTask(JakuRequest jakuRequest, RequestCallback requestCallback) {
        this.request = jakuRequest;
        setCallback(requestCallback);
    }

    /* access modifiers changed from: package-private */
    public void setCallback(RequestCallback requestCallback) {
        this.mCallback = requestCallback;
    }

    public static class Result {
        public Exception mException;
        public Object mResultValue;

        public Result(Object obj) {
            this.mResultValue = obj;
        }

        public Result(Exception exc) {
            this.mException = exc;
        }
    }

    /* access modifiers changed from: protected */
    public Result doInBackground(RokuRequestTypes... rokuRequestTypesArr) {
        Result result;
        if (isCancelled() || rokuRequestTypesArr == null || rokuRequestTypesArr.length <= 0) {
            return null;
        }
        RokuRequestTypes rokuRequestTypes = rokuRequestTypesArr[0];
        try {
            if (rokuRequestTypes.equals(RokuRequestTypes.query_active_app)) {
                result = new Result((List) this.request.send().getResponseData());
            } else if (rokuRequestTypes.equals(RokuRequestTypes.query_device_info)) {
                result = new Result(Device.Companion.fromDevice((com.jaku.model.Device) this.request.send().getResponseData()));
            } else if (rokuRequestTypes.equals(RokuRequestTypes.query_icon)) {
                result = new Result(((ByteArrayOutputStream) this.request.send().getResponseData()).toByteArray());
            } else {
                this.request.send();
                return null;
            }
            return result;
        } catch (Exception e2) {
            e2.printStackTrace();
            return new Result(e2);
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
        RequestCallback requestCallback;
        if (result != null && (requestCallback = this.mCallback) != null) {
            if (result.mException != null) {
                requestCallback.onErrorResponse(result);
            } else if (result.mResultValue != null) {
                requestCallback.requestResult(this.rokuRequestType, result);
            }
        }
    }
}
