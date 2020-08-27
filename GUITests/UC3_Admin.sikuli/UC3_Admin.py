#    UC3 - Delete user account
#
#    User can't delete his own account. 
#    So we take care of the elimination by the admin. 
#    We create a new account and then Admin will delete it
#
#    Registered on a 1920x1080p, Google Chrome 100% zoom


            ###  SETUP
click("1590678880209.png")
click("1590678953637.png")
type("1590829921729.png" , "AccoutToDelete" + Key.TAB + "delete@polito.it" + Key.TAB + "Delete1")
click("1590679157604.png")
click("1590788841790.png")
click("1590678880209-1.png")
wait(3)
type("1590829943940.png", "admin@ezgas.com" + Key.TAB + "admin" )
click("1590784293656.png")
wait(3)
click("1590784369122.png")
wait(3)


click(Pattern("1590920090357.png").targetOffset(556,-6))    #We use Offset to selecte the correct 'remove' button
wait(3)
click("1590788397797.png")
wait(3)
click("1590827727142.png")
wait(3)
click("1590788458524.png")
wait(2)