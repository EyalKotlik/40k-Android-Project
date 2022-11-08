import pandas


class Model:
    def __init__(self, name):
        Datasheets_models = pandas.read_csv(
            "Processed Data/Datasheets_models.csv", sep='|')
        model_data = Datasheets_models[Datasheets_models['name'] == name]
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
        if sv.isnumeric():
            self.sv = save("armor", int(sv))  # the model's armor save
        else:
            self.sv = save("daemonic", sv)  # the model's daemonic save
        # the model's points cost
        self.cost = int(model_data['Cost'].values[0])

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
        self.wargear = [Wargear(wargear) for wargear in self.wargear]
        print(self.wargear)


class save:
    def __init__(self, type, value):
        self.type = type
        if type != 'daemonic':
            self.ranged = value
            self.melee = value
        else:
            value = value.split('/')
            self.ranged = value[1]
            self.melee = value[0]


class Wargear:
    def __init(self, id):
        self.id = id
        wargear_data = pandas.read_csv("Processed Data/Wargear_list.csv", sep='|')
        wargear_data = wargear_data[wargear_data['wargear_id'] == id]


warrior = Model("necron warrior")
