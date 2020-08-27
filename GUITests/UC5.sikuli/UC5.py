#    UC5 - Modify Gas Station information
#    Precondition: Gas Station G exists
#
#    We modify gas Station created in Use Case 4 :
#            + Has Premium Gasoline
#            - Methane, Gasoline
#
#    Starting from homepage, not logged in
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
click("1590678880209-1.png")
wait(3)
type("1590829943940.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656-1.png")
wait(2)
click("1590784369122-1.png")
wait(2)
wheel(WHEEL_DOWN, 5)
wait(2)
type("1590830169812-1.png", "GasStation_UC5" + Key.TAB + "Torino, Corso Duca")
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
type("1590830389386-1.png", Key.DOWN + Key.DOWN + Key.ENTER)
click("1590830256446-1.png")
click("1590830265272-1.png")
wait(2)
click("1590785166092-1.png")
wait(3)
type(Key.HOME)
click("1590788397797-1.png")
wait(2)
click("1590828906996-1.png")
wait(2)
click("1590788458524-1.png")


#    Modify Gas Station information   
click("1590678880209.png")
wait(3)
type("1590830582295.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(2)
wheel(WHEEL_DOWN, 8)
wait(3)
click(Pattern("1590933877872.png").targetOffset(521,-31))    # We use Offset to select the 'Edit' button
wait(2)
wheel(WHEEL_UP, 4)
wait(5)
click("1590828464415.png")    # + Premium Gasoline 
wait(2)
click("1590828474470.png")    # - Gasoline
wait(2)
click("1590828526932.png")
wait(3)
type(Key.HOME)
click("1590828593323.png")
wait(2)
click("1590828628370.png")
wait(2)
click("1590828642394.png")
wait(2)



#    RESET
click("1590678880209.png")
wait(3)
type("1590830582295.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(2)
wheel(WHEEL_DOWN, 8)
wait(3)
click(Pattern("1590933877872.png").targetOffset(610,-30)) 
wait(3)

type(Key.HOME)
click("1590828593323.png")
wait(2)
click("1590828628370.png")
wait(2)
click("1590828642394.png")
wait(2)



