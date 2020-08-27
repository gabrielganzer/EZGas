#    UC2 - Modify user account
#    Precodintion: Account U exists
#
#    We create a new Account 'UserUc2' and then user will modify one or more fields
#
#    Starting from homepage, not logged in
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
click("1590678880209-1.png")
wait(2)
click("1590678953637.png")
wait(2)
type("1590829373120.png", "UserUC2" + Key.TAB + "user@polito.it" + Key.TAB + "pass1")
click("1590679157604.png")
click("1590788841790.png")

wait(3)


click("1590678880209.png")
type("1590684606245.png" , "user@polito.it" + Key.TAB + "pass1" ) 
wait(2)
click("1590684520387.png")
wait(3)
click("1590829484190.png")
wait(2)
type("1590829533230.png" , "newPass" )
click("1590829572951.png")
wait(2)
click("1590829600357.png")



#    RESET
wait(2)
click("1590678880209-3.png")
wait(2)
type("1590830099639.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(2)
click("1590784369122.png")
wait(2)
click(Pattern("1590932864637.png").targetOffset(564,-10))
wait(2)
click("1590788397797.png")
wait(2)
click("1590828906996.png")
wait(2)
click("1590788458524.png")
wait(2)

