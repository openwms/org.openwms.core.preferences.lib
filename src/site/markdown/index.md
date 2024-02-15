#Purpose
The OpenWMS.org Preferences Service deals with configuration and preferences for the whole application. It can be used to store
configuration parameters in different validity scopes. Scopes can be merged and inherited. Preferences might be stored only valid for a
particular *User* or a specific *Role*, specific to a *Module* (aka microservice) or the whole *Application*.

| scope       | applied to | inherits from |
| ----------- | ---------- | ------------- |
| User        | Valid for the particular User only. Overrides all other inherited preferences (when not set to force) | Role |
| Role        | Preferences assigned to a particular Role. These preferences are inherited to assigned Users. | Module |
| Module      | These kind of preferences are defined for a certain application module. The definition of the module boundaries is up to the application. | application |
| Application | Global preferences on application level. An application scoped preference | |
