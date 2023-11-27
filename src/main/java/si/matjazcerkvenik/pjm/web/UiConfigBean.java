/*
   Copyright 2023 Matjaž Cerkvenik

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package si.matjazcerkvenik.pjm.web;

import org.primefaces.event.SlideEndEvent;
import si.matjazcerkvenik.pjm.util.Props;
import si.matjazcerkvenik.pjm.util.Utils;
import si.matjazcerkvenik.pjm.util.PjmDateFormat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
@SuppressWarnings("unused")
public class UiConfigBean implements Serializable {

    private static final long serialVersionUID = 32054771235L;

    private int fadeOutFactor = 1;

    public String getVersion() {
        return Props.VERSION;
    }

    public boolean isContainerized() { return Props.IS_CONTAINERIZED; }

    public String getLocalIpAddress() {
        return Props.LOCAL_IP_ADDRESS;
    }

    public String getRuntimeId() {
        return Props.RUNTIME_ID;
    }

    public String getStartTime() {
        return Utils.getFormattedTimestamp(Props.START_UP_TIME, PjmDateFormat.DATE_TIME);
    }

    public String getUpTime() {
        int secUpTotal = (int) ((System.currentTimeMillis() - Props.START_UP_TIME) / 1000);
        return Utils.convertToDHMSFormat(secUpTotal);
    }

    public String getProjectsDirectoryPath() {
        return Props.PJM_PROJECTS_DIRECTORY;
    }

    public int getFadeOutFactor() {
        return fadeOutFactor;
    }

    public void setFadeOutFactor(int fadeOutFactor) {
        this.fadeOutFactor = fadeOutFactor;
    }

    public void onSlideEnd(SlideEndEvent event) {
        fadeOutFactor = (int) event.getValue();
    }

    public int[] getFadeOutConfig() {
        switch (fadeOutFactor) {
            case 1:
                int[] array1 = {1, 2, 3, 4};
                return array1;
            case 2:
                int[] array2 = {1, 4, 6, 8};
                return array2;
            case 3:
                int[] array3 = {2, 5, 9, 13};
                return array3;
            case 4:
                int[] array4 = {3, 6, 12, 21};
                return array4;
            case 5:
                int[] array5 = {4, 8, 15, 29};
                return array5;
        }
        int[] array0 = {1, 2, 3, 4};
        return array0;
    }
}
