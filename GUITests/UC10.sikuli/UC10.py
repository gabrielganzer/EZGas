#    UC10 - Evaluate price
#
#    User U exists  and has valid account

#     We create two Users, User1_UC10, User2_UC10 and one new gasStation GasStationUC10
#
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
#User1
click("1590678880209.png")
click("1590678953637.png")
wait(2)
type("1590829373120.png", "User1_UC10" + Key.TAB + "user1uc10@polito.it" + Key.TAB + "user1")
click("1590679157604.png")
click("1590788841790.png")
wait(2)

# User2
click("1590678880209.png")
wait(2)
click("1590678953637.png")
wait(2)
type("1590829373120.png", "User2_UC10" + Key.TAB + "user2uc10@polito.it" + Key.TAB + "user2")
click("1590679157604.png")
click("1590788841790.png")



#    Admin creates a new GasStation
click("1590678880209-1.png")
wait(3)
type("1590829943940.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(2)
click("1590784369122.png")
wait(2)
wheel(WHEEL_DOWN, 6)
wait(2)
type("1590830169812.png", "GasStation_UC10" + Key.TAB + "Torino, corso duca")
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
type("1590830389386.png", Key.DOWN + Key.DOWN + Key.ENTER)
click("1590830256446.png")
click("1590830265272.png")
wait(2)
click("1590785166092.png")
wait(3)
type(Key.HOME)
click("1590788397797.png")
wait(2)
click("1590828906996.png")
wait(2)
click("1590788458524.png")


#    User1 searches the gasStation
click("1590678880209.png")
wait(3)
type("1590829943940.png", "user1uc10@polito.it" + Key.TAB + "user1" )
click("1590784293656.png")
wait(2)
wheel(WHEEL_DOWN, 6)
type("1590931278631.png" , "Torino, corso duca" )
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
wait(2)
click("1590922172004.png")
wait(2)
wheel(WHEEL_DOWN, 4)
wait(2)
click(Pattern("1590922374562.png").targetOffset(543,-4))
wheel(WHEEL_DOWN, 4)
wait(2)
click(Pattern("1590930530512.png").targetOffset(73,1))
type("1.5")
click(Pattern("1590930568512.png").targetOffset(73,0))
type("1.4")
click("1590834482526.png")
wait(3)
type(Key.HOME)
wait(3)
click("1590788458524.png")



#    User2 login and evaluate prices
wait(2)
click("1590678880209.png")
wait(3)
type("1590829943940.png", "user2uc10@polito.it" + Key.TAB + "user2" )
click("1590784293656.png")
wait(2)
wheel(WHEEL_DOWN, 4)
wait(2)
type("1590918242822-1.png" , "Torino, corso duca" )
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
wait(2)
click("1590918499196.png")
wheel(WHEEL_DOWN, 3)

click(Pattern("1591638408351.png").targetOffset(1068,-3))
# User2 clicks on the green button if the price is correct, otherwise clicks on the red button
# If User clicks the green button, the User1 trustlevel increases +1, otherwise it decreases -1
#
wait(3)
type(Key.HOME)
click("1590788458524.png")
wait(2)



#    Admin deletes users and gasStation
click("1590678880209-1.png")
wait(3)
type("1590829943940.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(2)
click("1590784369122.png")
wait(2)
wheel(WHEEL_DOWN, 10)
wait(2)
click(Pattern("1590931822851.png").targetOffset(905,-27))
wait(2)
wheel(WHEEL_UP, 15)
wait(2)
click(Pattern("1590931876805.png").targetOffset(560,-4))
wait(2)
click(Pattern("1590931914901.png").targetOffset(556,-10))
wait(2)
click("1590788397797.png")
wait(2)
click("1590828906996.png")
wait(2)
click("1590788458524.png")
wait(2)

