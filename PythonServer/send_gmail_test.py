import smtplib
server = smtplib.SMTP("smtp.gmail.com:587")
server.starttls()

#Next, log in to the server
server.login("z58774556@gmail.com", "Zxc54683554")

#Send the mail
sent_from = 'z58774556@gmail.com'
to = ['h24563026@mailst.cjcu.edu.tw']
subject = 'OMG Super Important Message'
body = 'Hey, what"s up?\n\n- You'
email_text = """\
From: %s
To: %s
Subject: %s
%s
""" % (sent_from, ", ".join(to), subject, body)
server.sendmail("z58774556@gmail.com", "h24563026@mailst.cjcu.edu.tw", email_text)