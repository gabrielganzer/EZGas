#    UC4 - Create Gas Station
#    Precondition: Gas Station  G does not exist
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
type("1590830169812-1.png", "GasStation_UC4" + Key.TAB + "Torino, corso duca")
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
click(Pattern("1590933311758.png").targetOffset(613,-30))
wait(3)

type(Key.HOME)
click("1590788397797-1.png")
wait(2)
click("1590828906996-1.png")
wait(2)
click("1590788458524-1.png")

