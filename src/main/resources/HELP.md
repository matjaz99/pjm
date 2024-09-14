# Help

### Efforts

**Planned effort** is what managers think it would take to complete the project.  
**Estimated effort** is sum of all efforts on tasks, as they are estimated by assignees.

### Milestones

**Project start** is when project is started (aka. NPI-50 or B100)
**Planned end** is initial estimation when the project should end. Should not be changed once set.
**Expected end** is moving date when the project should end according to current progress.
**Completed end** is when project state was changed to `Completed`.

### Searching

Search for given text (operator contains) in:
- request title
- request description
- task title
- task description
- issues
- comments


**Remark 1:** currently only list of requests (containing searched string) is returned as result  
**Remark 2:** when using styling in description text, search may not work as expected because 
there are html tags inserted in the text.


### Search by label

Syntax: `<LABEL>: text`


Examples:
- `TAG: network` - search all tags containing word network
- `GROUP: security` - search by group
- `ASSIGNEE: John` - search by assignee (case sensitive!)



### Alarms

#### Alarm: No estimated effort

Alarm is raised for every requirement with sum of estimated efforts equal to 0.
Obsolete requirements are excluded.

#### Alarm: There are requirements without tasks

Each requirement should have at least one task. If there are no tasks, alarm is raised.

#### Alarm: Task is waiting for more than N days

If a task is in state `waiting` for more than 7 days, alarm is raised.

#### Alarm: Clarification is needed

There is a task in state `clarify`, alarm is raised.

#### Alarm: There are opened issues!

Obviously there are unresolved issues on the requirement and alarm is raised.
