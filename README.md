<<<<<<< HEAD
![GTIHUB](C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\toHealth_v2.png)

<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\google-play-badge.png" style="zoom: 40%;" />



# ToHealth

ToHealth is an app that can you to manage your health, including drug, fitness, sleep quality, even emotion record and create their social group to monitor each other.



## Home Page

<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\home_page (1).png" style="zoom:50%;" />

Home page will  show the to-do list shows item daily tasks. Use your finger to swipe on the screen and you can skip the mission or finish them rapidly.

- Arrange the to-do list by time point.

  ​	<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\home_toke (1).png" style="zoom:33%;" />

  1. The Medication, Events , Measure and Emotion Record are all different data sources from Firebase Datastore.

     ```kotlin
     val itemDataMediator = MediatorLiveData<MutableList<ItemDataType>>().apply {
         addSource(liveDrugs) { drugList ->
                               ...
                 //Create the todoListManager to arrange the todolist.
                 value = todoListManager.arrangeTodoList()
             }
         addSource(liveMeasure) { measureList ->
                 ...
             }
         addSource(liveEvent)...
         addSource(liveCare)...
         }
     ```

  2. Arrangement the todoList by several filter, data transformer (to ItemDataType) , and rearranger in **MediatorLiveData**.

     <img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\home_arranger (1).png" style="zoom: 33%;" />

  3. Calculate the all tasks and finished works during MediatorLiveData process and show it on progress bar.

     <img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\home_progress (1).png" style="zoom: 33%;" />

- Undo the to-do list item after swiping in 2 sec.

  1. If the user swipe to finished the task or skip the task, this feature can undo the last action and restore the list.

     ![](C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\home_Swipe (1).png)

     When user swipe the medication checked, the data will be store in temporary store list in viewmodel that provides the data for undo feature. If the data is the the last task at that time point, the header of time will be remove at the same time and store at the temporary store list. 

     After User swipe the next data or the un-do notification is gone, the data will post to Firebase Firestore, and remove the data in viewmodel.



## Group Page

<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\group_page (1).png" style="zoom:50%;" />

Your all social group list will be showed here, and you can see who in this group and all note and reminder below the member list. Next to the group name, the QR code is provide to new member join the group by camera.

 ![](C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\grouproom_page (1).png)

When user click "more" buttom, there are three page here, including board, chatting room and member list. The member list can let user review the member's statistic data and all tasks the member executed. If the member privede the authority for this group, you can edit the memebers task. 



## Statistic Page

<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\statistic_page (1).png" style="zoom: 50%;" />

Create the chart can be scrolled to show the task logs before and checkout the status, including finshed, unchecked and skipped. The measure data, like the blood pressure, blood oxigen, blood sugar and body temperature will show on the bar chart via **AnyChat**.



## Managment Page

<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\manage_page (1).png" style="zoom: 50%;" />

You can update the task or add the new task at this page. 

Here the task card will show the dose, execution time, start date, stock and status. If the stock is lower than ten times required, the alert notification will sent to you and your all group members and record on the alartmessage page.



## Background Works

<img src="C:\Users\ss890\AndroidStudioProjects\Personal Project\ToHealth\readmesrc\background (1).png" style="zoom:50%;" />

To monitor the database changes continusly in background within app activity is alive, the **Foreground** **Service** will create several listener to receive the changes of chat database and alart message.

When the user updates the task and swipe the task on to-do list, the the **AlartMessage** will be recieved an application with executed time setting.  The alartmessage will sent the Intent to **BroadcastReciever** and show the **notification** of the task to remind the user on time.



## Enviroment

Android Studio - Arctic Fox

Kotlin - 203-1.5.31-release-550-AS7717.8



## Library 

- AnyChart - 1.1.2
- Glide - 4.11.0
- RecyclerViewSwipeDecorator - 1.3
- Firebase
- Lottie



## Contect

ss890900@gmail.com



=======
![GITHUB](https://github.com/WeitingL/ToHealth/blob/8ddea9219697657635edb85e1b4571ebd5a28cd4/toHealth_v2%20.1.png "title image")

# ToHealth
>>>>>>> 1502f93270f45f49186967fe8dbfeb7073e6ba88
