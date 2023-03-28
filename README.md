## About The Project

TODO

## Architecture Guidelines 
I am trying to follow common clean code principles for android architecture: https://developer.android.com/topic/architecture

TODO: ADD DDD EXPLANAITION, ADD TDD EXPLANAITION, CLEAN ARCHITECTURE, USEFUL LINKS.

example projects

sotial network app (ddd feature layers example): https://github.com/philipplackner/SocialNetworkTwitch/tree/development/app/src/main/java/com/plcoding/socialnetworktwitch
clean architecture app: https://github.com/philipplackner/CleanArchitectureNoteApp/tree/app 
ddd real life example app: https://gitlab.com/flexsentlabs/tip_prorotype/-/tree/master/app/src/main/java/com/flexsentlabs/tip 

## Project Stracture

TODO

## Git Guildelines 
Here I am trying to implement all the common principles of Git Flow: https://nvie.com/posts/a-successful-git-branching-model/

As for now "Main" branch is empty and there are no "Release Branches", because I haven't posted anything on the Google Play platform yet. And the current "Develop" version of this app doesn't sutisfy all the user's needs yet. Therefore "Develop" branch acts as a working code source as for this moment, where all the latest updates are mearged into.

### Merge request branch creation

When you have finished writing code and want to commit:

- Create a new branch feature/TICKET-123_handle_order_loading

Where "TICKET-123" is the task/bug name and number and "handle order loading" is the title. 
Name of the branch can be: feature, bugfix, refactor (depending on the use case).
Don't forget that you need to branch off the current "develop" branch.
 
### Commit comments
 
Use the CHANGELOG notation https://keepachangelog.com/ru/1.0.0/ for marking [ADDED], [CHANGED], 
[REMOVED] or [FIXED] code.

Use the following template for commit messaging: 
```
TICKET-123 - handle order loading
[ADDED] OrderInteractor for order downloading
[CHANGED] file downloading via CDN
[REMOVED] ProfileActivity
[FIXED] image caching on disk
````

### Merge request reviewers

After you have created your feature/JIRA-123_add_avatar_loading branch from the current develop branch 
and pushed it to remote. Tag appropriate people to review your merge request.

## Libraries 

TODO
