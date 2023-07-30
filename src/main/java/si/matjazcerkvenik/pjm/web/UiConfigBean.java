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

import si.matjazcerkvenik.pjm.Props;
import si.matjazcerkvenik.pjm.util.Formatter;
import si.matjazcerkvenik.pjm.util.PjmDateFormat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
@SuppressWarnings("unused")
public class UiConfigBean implements Serializable {

    private static final long serialVersionUID = 32054771235L;

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
        return Formatter.getFormatedTimestamp(Props.START_UP_TIME, PjmDateFormat.DATE_TIME);
    }

    public String getUpTime() {
        int secUpTotal = (int) ((System.currentTimeMillis() - Props.START_UP_TIME) / 1000);
        return Formatter.convertToDHMSFormat(secUpTotal);
    }

    public String getProjectsDirectoryPath() {
        return Props.PJM_PROJECTS_DIRECTORY;
    }

}
