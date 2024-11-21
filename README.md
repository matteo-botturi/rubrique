# **Rubrique de Contacts**

A JavaFX application for managing contacts efficiently. This app provides functionalities for adding, editing, deleting, and searching contacts, along with saving them to and loading them from files.

---

## **Features**

- **Contact Management**: 
  - Add, edit, and delete contacts.
  - View contact details in a user-friendly interface.
- **File Operations**: 
  - Save contacts to a file.
  - Load contacts from an existing file.
- **Search Functionality**: 
  - Filter contacts by name and surname in real-time.
- **Recent Files**: 
  - Quick access to recently opened files.
- **Responsive Interface**: 
  - Dynamically updates the application state in response to user actions.
- **Clock Integration**:
  - Displays the current date and time at the bottom of the application.

---

## **Usage**

### **Main Functionalities**

#### **Add a Contact:**

1. Click the "New" button.
2. Fill in the contact details.
3. Click "OK" to save.

#### **Edit a Contact:**

1. Select a contact from the table.
2. Click the "Edit" button.
3. Update the details and click "OK".

#### **Delete a Contact:**

1. Select a contact from the table.
2. Click the "Delete" button.

#### **Search Contacts:**

- Use the search fields to filter contacts by name or surname.

#### **File Management:**

- Save the current contact list to a file.
- Open an existing contact file.

---

## **Project Structure**
```
src/
├── main/
│   ├── java/
│   │   ├── model/
│   │   │   ├── Person.java
│   │   │   └── DirectoryBean.java
│   │   ├── util/
│   │   │   ├── DateUtility.java
│   │   │   ├── TitleUpdater.java
│   │   │   └── AlertHelper.java
│   │   ├── controller/
│   │   │   ├── MenuController.java
│   │   │   ├── PersonOverviewController.java
│   │   │   └── PersonEditDialogController.java
│   │   └── MainApp.java
│   └── resources/
│       ├── fxml/
│       │   ├── RootLayout.fxml
│       │   ├── PersonOverview.fxml
│       │   └── PersonEditDialog.fxml
│       └── images/
│           └── icon.png
```
