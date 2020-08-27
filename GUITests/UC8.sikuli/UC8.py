#    UC8 - Obtain price of fuel for gas stations in a certain geographic area
#
#    The anonymous user AU selects a geo point GP...
#    Admin creates two new GasStationes and at the end will delete them (Reset)
#
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
click("1590678880209.png")
wait(3)
type("1590830099639.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(3)
wheel(WHEEL_DOWN, 4)    # The 'scrool' depends on the size of the DB
wait(3)
type("1590830169812-1.png", "GasStation_UC8__1" + Key.TAB + "Torino, Corso Duca")
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
type("1590830389386.png", Key.DOWN + Key.DOWN + Key.ENTER)
click("1590830256446.png")
click("1590830265272.png")
wait(3)
click("1590785166092.png")
wait(3)
type("1590830169812-1.png", "GasStation_UC8__2" + Key.TAB + "Torino, Corso Duca")
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
type("1590830389386.png", Key.DOWN + Key.DOWN + Key.ENTER)
click("1590830256446.png")
click("1590830265272.png")
wait(3)
click("1590785166092.png")
wait(3)
wheel(WHEEL_DOWN, 20)
type("1590946014025.png", "Torino, Corso Duca")
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
wait(3)
click("1590946076018.png")
wheel(WHEEL_DOWN, 4)
wait(3)
click(Pattern("1590947951701.png").targetOffset(1056,-5))
wait(3)
wheel(WHEEL_UP, 4)
wait(3)
click(Pattern("1590834384562.png").targetOffset(175,-2))
type("1.1")
click(Pattern("1590834882782.png").targetOffset(160,1))
type("1.5")
wait(3)
click("1590834482526.png")
wait(3)
wheel(WHEEL_DOWN, 8)
wait(3)
click(Pattern("1590947972249.png").targetOffset(1056,-7))
wheel(WHEEL_UP, 4)
wait(3)
click(Pattern("1590834384562.png").targetOffset(175,-2))
type("1.5")
click(Pattern("1590834882782.png").targetOffset(160,1))
type("1.1")
wait(3)
click("1590834482526.png")
wait(3)
type(Key.HOME)
click("1590832381051.png")
wait(2)
click("1590832526665.png")
wait(5)


#    An User  
wheel(WHEEL_DOWN, 6)
wait(2)
type("1590947843719.png" , "Torino, Corso Duca" )
wait( "add_UC10.png" , 20)
type(Key.DOWN + Key.ENTER)
click("1590918438260.png")    
type(Key.DOWN + Key.DOWN + Key.ENTER)    # In this way we select 'Gasoline'
click("1590919031268.png")
type(Key.DOWN + Key.ENTER)    # We select 'Enjoy' as car sharing
click("1590918499196.png")
wait(1)
wheel(WHEEL_DOWN, 3)

    # Now we have the 'List of Gas Station' and we can have different variantes:
    # we can sort by Diesel price / Gasoline price ecc..

#We have 5 different 'Sort Botton' for our different fuel types. In order: "Diesel  Gasoline  Premium Gasoline  LPG  Methane"
#Using the offset we can select the desired button
wait(3)
click(Pattern("1590948676576.png").targetOffset(-60,-4))    #Second botton -> sorted by Dasoline Price
wait(3)

click(Pattern("1590948712270.png").targetOffset(-171,2))    #first button -> sorted by Diesel price
wait(3)


#    RESET
click("1590678880209.png")
wait(3)
type("1590830099639.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(3)
wheel(WHEEL_DOWN, 10)
wait(2)
click(Pattern("1590947288456.png").targetOffset(1089,-31))    #Offset to select the 'Remove' button
wait(1)
click(Pattern("1590947328122.png").targetOffset(1084,-38))
wait(3)
type(Key.HOME)
click("1590832381051.png")
wait(2)
click("1590832526665.png")
wait(5)
