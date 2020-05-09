# Protocol Companion
Protocol Companion is a mobile companion app for Android to be used with Android Wear devices. 
It allows users to create and modify research study parameters efficiently and send the information 
to Wear devices for use in health research.

# General flow of the application

## Functionality: Display study list
### Files involved: HomeFragment.java, MyStudyRecyclerViewAdapter.java, fragment_home.xml, and fragment_home_list.xml
The home/startup page of the application is the list of all studies stored in protocols.json and loaded 
into the ITEMS map in the Study class. The RecyclerViewAdapter takes all the studies in the list of 
values in ITEMS, and formats them to display the study name (the fields chosen to be displayed are completely 
customizable, you can choose any Study field or display other data as well).

## Functionality: Create study
### Files involved: CreateStudyFragment.java, fragment_createstudy.xml
A user can select "Create New Study" from the navigation drawer. They will be taken to the create study
fragment, where they can input details about the new study and click "Save" to save the study to the list
of studies (and the protocols.json file for persistent storage).

## Functionality: View study details / Edit study
### Files involved: StudyDetailFragment.java, fragment_studydetail.xml
A user can select any study from the study list to view and edit its details. They will be taken to the 
edit study fragment, where they can view or change details about the study and click "Save" to commit
the changes (and the protocols.json file for persistent storage).

## Functionality: Delete study
### Files involved: StudyDetailFragment.java, fragment_studydetail.xml
A user can delete a study by either long-pressing the study from the study list and choosing "Delete study"
or by viewing the study's details and selecting the button to "Delete study" at the bottom.

## Functionality: Share study
### Files involved: HomeFragment.java, MyStudyRecyclerViewAdapter.java, fragment_home.xml, and fragment_home_list.xml, context_menu.xml
A user can share a study by long-pressing the study from the study list and choosing "Share study" and
a share chooser will pop up, allowing the user to share the json text of ALL studies (todo: just selected study)

## Functionality: Load study from QR code
### Files involved: LoadStudyFragment.java, fragment_loadstudy.xml
A user can load a study from a QR code by selecting "Load Study from QR Code" from the navigation drawer
and using their camera to scan a QR code. The QR code represents a URL, which is followed in order to 
download the JSON file (from Dropbox, for example). The file is then imported.

## Functionality: Send study to watch (currently unimplemented)
### Files involved: StudyDetailFragment.java, fragment_studydetail.xml, context_menu.xml
Option to press "Send to Watch" button in the study detail page, or option to choose "Send to Watch"
menu option in the context menu that appears on long press of a study.

# File breakdown

## MainActivity.java
### Layout file: activity_main.xml and content_main.xml
The main/wrapper activity that hosts the fragment returned by the navigation controller.

## HomeFragment.java and MyStudyRecyclerViewAdapter.java
### Layout files: fragment_home.xml and fragment_home_list.xml
Deals with the generation and display of the list of studies on the home page.

## CreateStudyFragment.java
### Layout file: fragment_createstudy.xml
Users can create new studies.

## StudyDetailFragment.java
### Layout file: fragment_studydetail.xml
Users can view and edit the details of new studies.

## LoadStudyFragment.java
### Layout file: fragment_loadstudy.xml
Loads a study/studies from a QR code.

## Study.java
The Study model defines the fields and methods pertaining to each study.

## StudyViewModel.java
The view model (MVVM framework) / controller (MVC framework) that connect the Study model to the fragment views.

## NetworkFragment and DownloadCallback.java
Downloads a file from the URL obtained by scanning a QR code.

## Context Menu
### Layout file: context_menu.xml
When a user long presses on a study in the study list, this context menu is activated. Currently allows
the user to edit, delete, or share a study, as well as providing the framework to allow the user to 
send the study to a watch.

## Navigation
### Layout files (navigation drawer): activity_main_drawer.xml, app_bar_main.xml, nav_header_main.xml
### Navigation graph: mobile_navigation.xml
Navigation graph of how each page can be reached from other pages. Connections between pages are called
Actions and can have parameters that will be passed from the source page to the destination page.
The home page (HomeFragment currently) is specified here as well.

### strings.xml
Contains all the hardcoded strings in the app, so all strings can be referenced by string ID instead
of the hardcoded value. When a value in this file is changed, it's automatically changed everywhere
else. Reduces coupling and errors.

## Local storage file
### protocols.json
Currently, studies are persistently stored locally in protocols.json in JSON format with the ID of each
study as the key and a nested JSON representation of the study as the value.

# Links to JSON testing files with study/studies pre-loaded.
These can be used to test downloading a file from a QR code by generating a QR code that represents
the following links. The application will follow the link from the QR code, download the JSON, and 
import the study/studies it contains into its study list.

###test1study.json
https://www.dropbox.com/s/ibr8x0831ze7xxm/test1study.json?dl=1

###test2studies.json
https://www.dropbox.com/s/k7apsscrnoavgk3/test2studies.json?dl=1


