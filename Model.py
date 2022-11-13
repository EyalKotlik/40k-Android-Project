import math

import pandas


class Model:
    def __init__(self, name):
        Datasheets_models = pandas.read_csv(
            "Processed Data/Datasheets_models.csv", sep='|')
        model_data = Datasheets_models[Datasheets_models['name'] == name]
        if model_data.empty:
            raise Exception("Model not found")
        self.name = name
        self.id = int(model_data['datasheet_id'].values[0])  # the model's id
        # the model's line (neccessary to identify some models)
        self.line = int(model_data['line'].values[0])
        self.ws = int(model_data['WS'].values[0])  # the model's weapon skill
        # the model's ballistic skill
        self.bs = int(model_data['BS'].values[0])
        self.s = int(model_data['S'].values[0])  # the model's strength
        self.t = int(model_data['T'].values[0])  # the model's toughness
        self.w = int(model_data['W'].values[0])  # the model's wounds
        self.a = int(model_data['A'].values[0])  # the model's attacks
        sv = model_data['Sv'].values[0]
        self.saves = dict()  # the model's saves, the key is the save type (armour, daemonic, invulnerable), the value is the tuple (melee save, ranged save)
        if sv.isnumeric():
            self.saves["armour"] = (int(sv), int(sv))
        else:
            sv = sv.split('/')
            self.saves["daemonic"] = (int(sv[0]), int(sv[1]))
        # the model's points cost
        if not math.isnan(model_data['Cost'].values[0]):
            self.cost = int(model_data['Cost'].values[0])
        else:
            self.cost = 0
            

        # the model's keywords
        keywords_data = pandas.read_csv(
            "Processed Data/Datasheets_keywords.csv", sep='|')
        self.keywords = list(
            keywords_data[keywords_data['datasheet_id'] == self.id]['keyword'].values)

        # the model's wargear
        wargear_data = pandas.read_csv(
            "Processed Data/Datasheets_wargear.csv", sep='|')
        self.wargear = list(
            wargear_data[wargear_data['datasheet_id'] == self.id]['wargear_id'].values)
        self.wargear = [Wargear(int(wargear)) for wargear in self.wargear]

        # the model's abilities
        self.abilities = list()  # the model's abilities

    def __str__(self):
        output = "Name=" + self.name + "\nWS=" + str(self.ws) + " BS=" + str(self.bs) + " S=" + str(self.s) + " T=" + str(
            self.t) + " W=" + str(self.w) + " A=" + str(self.a) + " Sv=" + str(self.saves) + " Cost=" + str(self.cost)
        output += "\nKeywords=" + str(self.keywords)
        output += "\nWargear="
        for wargear in self.wargear:
            output += "\n   " + str(wargear)
        output += "\nAbilities=" + str(self.abilities)
        return output


class Wargear:
    def __init__(self, id):
        self.id = id
        wargear_data = pandas.read_csv("Processed Data/Wargear.csv", sep='|')
        wargear_data = wargear_data[wargear_data['id'] == id]
        self.name = wargear_data['name'].values[0]
        if type(wargear_data['description'].values[0]) is not float:
            self.profile_choice = "inclusive" if "before selecting targets, select one or both of the profiles below to make attacks with. if you select both," in str(
                wargear_data['description'].values[0]) else "exclusive"
        else:
            self.profile_choice = "exclusive"
        wargear_data = pandas.read_csv(
            "Processed Data/Wargear_list.csv", sep='|')
        wargear_data = wargear_data[wargear_data['wargear_id'] == id]
        # the profile (stats) of the wargear - necessary because some wargear have multiple different profiles
        self.profiles = list(WargearProfile(id, int(line)+1)
                             for line in range(len(wargear_data)))
        self.abilities = list()  # the wargear's abilities

    def __str__(self):
        output = "name=" + self.name
        if len(self.profiles) == 1:
            output += "; profile=" + str(self.profiles[0])
        else:
            output += "; profile choice=" + self.profile_choice
            for profile in self.profiles:
                output += "\n   " + str(profile)
        return output


class WargearProfile:
    def __init__(self, id, line):
        self.id = id
        self.line = line
        wargear_data = pandas.read_csv(
            "Processed Data/Wargear_list.csv", sep='|')
        wargear_data = wargear_data[wargear_data['wargear_id'] == id]
        if len(wargear_data) > 1:
            wargear_data = wargear_data[wargear_data['line'] == line]
        self.name = wargear_data['name'].values[0]  # the wargear's name
        self.type = wargear_data['type'].values[0]  # the wargear's type
        if self.type != "melee":
            self.type = self.type.split(' ')
            if self.type[0] == "rapid" and self.type[1] == "fire":
                self.type = ["rapid fire", self.type[2]]
        self.range = wargear_data['Range'].values[0]  # the wargear's range
        self.s = wargear_data['S'].values[0]  # the wargear's strength
        # the wargear's armor penetration
        self.ap = wargear_data['AP'].values[0]
        self.d = wargear_data['D'].values[0]  # the wargear's damage
        self.abilities = list()  # the wargear profile's abilities

    def __str__(self):
        if self.type != "melee":
            output = "name=" + self.name + ", type=" + \
                "("+self.type[0]+","+self.type[1]+")" + ", range=" + \
                self.range + ", s=" + self.s + ", ap=" + self.ap + ", d=" + self.d
        else:
            output = "name=" + self.name + ", type=" + \
                self.type + ", range=" + \
                self.range + ", s=" + self.s + ", ap=" + self.ap + ", d=" + self.d
        return output


warrior = Model("triarch stalker")
print(warrior.__str__())
