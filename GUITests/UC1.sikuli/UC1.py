#    UC1 - Create User Account
#      
#    We create a new User "UserUC1" and then Admin will delete it.
#
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
#    New User
click("1590678880209.png")
wait(2)
click("1590678953637.png")
wait(2)
type("1590829373120.png", "UserUC1" + Key.TAB + "user@polito.it" + Key.TAB + "pass1")
click("1590679157604.png")
click("1590788841790.png")



#    RESET
wait(2)
click("1590678880209-1.png")
wait(2)
type("1590830099639.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(2)
click("1590784369122.png")
wait(2)
click(Pattern("1590932401751.png").targetOffset(561,-13))
wait(2)
click("1590788397797.png")
wait(2)
click("1590828906996.png")
wait(2)
click("1590788458524.png")
wait(1)
