#Preferences

##Purpose
The OpenWMS.org Preferences microservice deals with configuration and preferences on application level. It can be used to store preferences
in different scopes. Scopes can inherit preferences from each other. In the current version, four different scopes are supported with the
following inheritance strategy:

| scope       | applied to | inherits from |
| ----------- | ---------- | ------------- |
| User        | Valid for the particular User only. Overrides all other inherited preferences (when not set to force) | Role |
| Role        | Preferences assigned to a particular Role. These preferences are inherited to assigned Users. | Module |
| Module      | These kind of preferences are defined for a certain application module. The definition of the module boundaries is up to the application. | application |
| Application | Global preferences on application level. An application scoped preference | |
