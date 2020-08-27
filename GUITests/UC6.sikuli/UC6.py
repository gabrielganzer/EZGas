#    UC6 - Delete Gas Station
#
#    Precondition: Gas Station G exists
#    Admin creates a new GasStation and then deletes it
#
#    Starting from homepage, Admin not logged
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
click("1590678880209.png")
wait(3)
type("1590830582295.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(3)
wheel(WHEEL_DOWN, 5)
wait(3)
type("1590830169812-1.png", "GasStation_UC6" + Key.TAB + "Torino, corso duca")
wait( "add_UC10.png" , 20)

type(Key.DOWN + Key.ENTER)

type("1590830389386.png", Key.DOWN + Key.DOWN + Key.DOWN + Key.ENTER)
click("1590831790058.png")
wait(2)
click("1590785166092.png")
wait(4)
wheel(WHEEL_DOWN, 5)
wait(6)

click(Pattern("1590935491983.png").targetOffset(614,-22))    # We use Offset to select the 'Remove' button frome the correct GasStation
wait(3)
type(Key.HOME)
click("1590832381051.png")
wait(2)
click("1590832526665.png")
wait(2)