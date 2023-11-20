#1.Belaso cipher - codificare si decodificare

def BelasoCipherEncription(alphabet, keyword, plaintext):
    n = len(alphabet) #the length of the alphabet; we will need it when we do the encription
    keyword = keyword.lower()
    plaintext = plaintext.lower() #the letters of the alphabet are lower-case, we need to convert the keyword and the plaintext, in case they are uppercase

    keyword_letter_position = []
    for i in range(len(keyword)): #in this loop, we save in a list the positions in the alphabet of all the keyword's letters
        letter = keyword[i]
        for pos in range(len(alphabet)): #we search the position of the letter in the alphabet
            if alphabet[pos] == letter:
                keyword_letter_position.append(pos)
                break
    print("K= ", keyword_letter_position)

    plaintext_letter_position = []
    for i in range(len(plaintext)): #we do the same thing for the plaintext as we did for the keyword, we save the positions of the letters
        letter = plaintext[i]
        for pos in range(len(alphabet)):
            if alphabet[pos] == letter:
                plaintext_letter_position.append(pos)
                break
    print("Numerical: ", plaintext_letter_position)

    i = 0
    while len(keyword_letter_position) < len(plaintext_letter_position): #this loop is necesarry because we want to have the lists of the same length, it will be easier to encode that way
        keyword_letter_position.append(keyword_letter_position[i])
        i += 1
    print("K= ", keyword_letter_position)

    encription = []
    for i in range(len(plaintext_letter_position)): #in this loop, we add the positions of the letters of the keyword with the ones of the plaintext
        encription.append((keyword_letter_position[i] + plaintext_letter_position[i])%n) # modulo n is there because the sum might be bigger than the length of the alphabet
    print("Encription: ", encription)

    ciphertext = ""
    for i in range(len(encription)):
        ciphertext += alphabet[encription[i]] #we already have the positions of the letters of the ciphertext, so we just concatenate the letters

    print("Ciphertext: ", ciphertext.upper())

def BelasoCipherDecription(alphabet, keyword, ciphertext):
    n = len(alphabet) #the length of the alphabet; we will need it when we do the encription
    keyword = keyword.lower()
    ciphertext = ciphertext.lower() #the letters of the alphabet are lower-case, we need to convert the keyword and the plaintext, in case they are uppercase

    keyword_letter_position = []
    for i in range(len(keyword)): #in this loop, we save in a list the positions in the alphabet of all the keyword's letters
        letter = keyword[i]
        for pos in range(len(alphabet)): #we search the position of the letter in the alphabet
            if alphabet[pos] == letter:
                keyword_letter_position.append(pos)
                break
    print("K= ", keyword_letter_position)

    ciphertext_letter_position = []
    for i in range(len(ciphertext)): #we do the same thing for the ciphertext as we did for the keyword, we save the positions of the letters
        letter = ciphertext[i]
        for pos in range(len(alphabet)):
            if alphabet[pos] == letter:
                ciphertext_letter_position.append(pos)
                break
    print("Encription: ", ciphertext_letter_position)

    i = 0
    while len(keyword_letter_position) < len(ciphertext_letter_position): #this loop is necesarry because we want to have the lists of the same length, it will be easier to decode that way
        keyword_letter_position.append(keyword_letter_position[i])
        i += 1
    print("K= ", keyword_letter_position)

    plaintext_letter_position = []
    for i in range(len(ciphertext_letter_position)): #we have the positions of the encripted version, here we transform this to the numerical version
        plaintext_letter_position.append((ciphertext_letter_position[i] - keyword_letter_position[i])%n)
    print("Numerical: ", plaintext_letter_position)

    plaintext = ""
    for i in range(len(plaintext_letter_position)): #we already have the positions of the letters of the plaintext, so we just concatenate the letters
        plaintext += alphabet[plaintext_letter_position[i]]

    print("Plaintext: ", plaintext.upper())


if __name__ == '__main__':
    alphabet = [' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']
    keyword = input("Which is the keyword?")
    answer = int(input("Do you want to encrypt (1) or decrypt (2)?"))
    if (answer == 1):
        plaintext = input("Which is the plaintext?")
        BelasoCipherEncription(alphabet, keyword, plaintext)
    elif (answer == 2):
        ciphertext = input("Which is the ciphertext?")
        BelasoCipherDecription(alphabet, keyword, ciphertext)
