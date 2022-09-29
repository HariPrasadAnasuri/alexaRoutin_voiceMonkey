To start the motion check:
    cd ~/pi/Camera/OpenCV
    python pi_surveillance_auth.py --conf conf.json

To start the application:
    cd ~/pi/artifacts/AlexaVoiceMonkeyApp
    java -jar alexaroutin_voicemonkey-0.0.1-SNAPSHOT.jar
To start ngrok server
    sudo -s
    cd Downloads/ngrok-v3-stable-linux-arm64
    ngrok start --all