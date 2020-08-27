#    UC7 - Report fuel price for a gas station
#
#    Precondition: Gas station G exists, User U is registered in the system
#    We use User 'Marco1' from UC1/UC2 and a new GasStation
#
#    Starting from homepage, not logged in
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
            
click("1590678880209.png")
wait(3)
type("1590830099639.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(3)
wheel(WHEEL_DOWN, 6)    # The 'scrool' depends on the size of the DB
wait(3)
type("1590830169812-1.png", "GasStation_UC7" + Key.TAB + "Torino, corso duca")
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
wait(2)
type("1590830389386.png", Key.DOWN + Key.DOWN + Key.ENTER)
click("1590830256446.png")
click("1590830265272.png")
wait(2)
click("1590785166092.png")
wait(2)        # to see the GasStation_UC7 inserted in the 'List of Gas Stations'
wheel(WHEEL_DOWN, 6)
wait(3)
type(Key.HOME)     
click("1590788397797.png")
wait(1)
click("1590828906996.png")
wait(1)
click("1590788458524.png")


#    Setup User
wait(2)
click("1590678880209-1.png")
wait(2)
click("1590678953637.png")
wait(2)
type("1590829373120.png", "UserUC7" + Key.TAB + "user@polito.it" + Key.TAB + "pass1")
click("1590679157604.png")
click("1590788841790.png")
wait(3)   
click("1590832935657.png")
wait(1)
type("1590830582295.png", "user@polito.it" + Key.TAB + "pass1" ) 
wait(1)
click("1590684520387.png")
wait(5)
wheel(WHEEL_DOWN, 10)
type("1590931278631.png" , "Torino, Corso Duca" )
wait( "add_UC10-1.png" , 20)
type(Key.DOWN + Key.ENTER)
wait(2)
click("1590922172004.png")
wait(2)
wheel(WHEEL_DOWN, 4)
wait(2)
click(Pattern("1590936277925.png").targetOffset(516,-18))
wheel(WHEEL_DOWN, 10)
click(Pattern("1590834384562.png").targetOffset(175,-2))
type("1.7")
click(Pattern("1590834882782.png").targetOffset(160,1))
type("1.7")
wait(1)
click("1590834482526.png")
wait(2)

type("r",  KeyModifier.CTRL)  # Reload the page to see new fuel Price
wait(2)

click("1590835119730.png")
wheel(WHEEL_DOWN, 10)
type("1590931278631.png" , "Torino, Dorso Duca" )
wait( "add_UC10-1.png" , 20)
type(Key.DOWN + Key.ENTER)
wait(2)
click("1590922172004.png")
wheel(WHEEL_DOWN, 10)
wait(5)
type(Key.HOME)


# Logout
click("1590834592175.png")


#    RESET
wait(2)
click("1590678880209-3.png")
wait(2)
type("1590830099639-1.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656-2.png")
wait(2)
click("1590784369122-2.png")
wait(2)
wheel(WHEEL_DOWN, 10)
wait(2)
click(Pattern("1590936830717.png").targetOffset(1105,-28))    # Delete GasStation_UC7
wait(3)
type(Key.HOME)
click(Pattern("1590936770707.png").targetOffset(561,-17))    # Delete UserUC7
wait(2)
click("1590828593323.png")
wait(2)
click("1590828628370.png")
wait(2)
click("1590828642394.png")
wait(2)





