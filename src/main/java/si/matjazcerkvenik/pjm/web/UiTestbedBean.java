package si.matjazcerkvenik.pjm.web;

import org.primefaces.component.colorpicker.ColorPicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.Tag;
import si.matjazcerkvenik.pjm.util.DAO;
import si.matjazcerkvenik.pjm.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class UiTestbedBean extends UiBean implements Serializable {

    private static final long serialVersionUID = 2104801510L;

    private static final Logger logger = LoggerFactory.getLogger(UiTestbedBean.class);

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = requestParameterMap.getOrDefault("projectId", null);
        project = uiAppBean.getProject(id);
        logger.info("loaded " + project.getName());
    }


    // for primefaces 14
//    private DefaultDiagramModel model;
//
//    public DiagramModel getModel() {
//        model = new DefaultDiagramModel();
//        model.setMaxConnections(-1);
//        model.setConnectionsDetachable(false);
//
//        Element elementA = new Element("A", "20em", "6em");
//        elementA.addEndPoint(new DotEndPoint(EndPointAnchor.BOTTOM));
//
//        Element elementB = new Element("B", "10em", "18em");
//        elementB.addEndPoint(new DotEndPoint(EndPointAnchor.TOP));
//
//        Element elementC = new Element("C", "40em", "18em");
//        elementC.addEndPoint(new DotEndPoint(EndPointAnchor.TOP));
//
//        model.addElement(elementA);
//        model.addElement(elementB);
//        model.addElement(elementC);
//
//        model.connect(new Connection(elementA.getEndPoints().get(0), elementB.getEndPoints().get(0)));
//        model.connect(new Connection(elementA.getEndPoints().get(0), elementC.getEndPoints().get(0)));
//        return model;
//    }
    
}
