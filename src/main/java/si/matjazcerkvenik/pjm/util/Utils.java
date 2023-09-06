package si.matjazcerkvenik.pjm.util;

import si.matjazcerkvenik.pjm.model.Project;
import si.matjazcerkvenik.pjm.model.Requirement;
import si.matjazcerkvenik.pjm.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static List<Requirement> getRequirementsWithoutTasks(Project project) {
        List<Requirement> result = project.getRequirements().getList().stream()
                .filter(req -> (req.getTasks() == null || req.getTasks().getList() == null || req.getTasks().getList().size() == 0))
                .collect(Collectors.toList());
        return result;
    }

    public static List<Requirement> getRequirementsWithTaskStatus(Project project, String status) {
        List<Requirement> list = new ArrayList<>();
        for (Requirement r : project.getRequirements().getList()) {
            if (r.getTasks() != null && r.getTasks().getList() != null) {
                for (Task t : r.getTasks().getList()) {
                    if (t.getStatus() != null && t.getStatus().equalsIgnoreCase(status)) list.add(r);
                }
            }
        }
        return list;
    }

}
