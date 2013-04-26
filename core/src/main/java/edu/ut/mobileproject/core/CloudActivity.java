package edu.ut.mobileproject.core;

import java.lang.reflect.Method;
import android.app.Activity;

/**
 * Created with IntelliJ IDEA.
 * User: codemaus
 * Date: 3/9/13
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CloudActivity extends Activity {
    protected CloudController cloudController = new CloudController();
    private CloudActivity localState;

    protected CloudActivity() {
    }

    public CloudActivity(CloudController cc) {
        cloudController = cc;
    }

    public CloudController getCloudController() {
        return cloudController;
    }

    public void setCloudController(CloudController cloudController) {
        this.cloudController = cloudController;
    }


    public void copyState(CloudActivity cs) {
        this.localState =  cs;
    }

   //public abstract Object execute(Method toExecute, Class<?>[] paramTypes, CloudActivity cloudModel);


}
