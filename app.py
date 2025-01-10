import streamlit as st
from PIL import Image
import io
import numpy as np
import os
os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'
import tensorflow as tf
from utils import clean_image, get_prediction, make_results

@st.cache_resource
def load_model(path):
    xception_model = tf.keras.models.Sequential([
        tf.keras.applications.xception.Xception(include_top=False, weights='imagenet', input_shape=(512, 512, 3)),
        tf.keras.layers.GlobalAveragePooling2D(),
        tf.keras.layers.Dense(4, activation='softmax')
    ])
    
    densenet_model = tf.keras.models.Sequential([
        tf.keras.applications.densenet.DenseNet121(include_top=False, weights='imagenet', input_shape=(512, 512, 3)),
        tf.keras.layers.GlobalAveragePooling2D(),
        tf.keras.layers.Dense(4, activation='softmax')
    ])
    
    inputs = tf.keras.Input(shape=(512, 512, 3))
    xception_output = xception_model(inputs)
    densenet_output = densenet_model(inputs)
    outputs = tf.keras.layers.average([densenet_output, xception_output])
    model = tf.keras.Model(inputs=inputs, outputs=outputs)
    model.load_weights(path)
    return model

hide_streamlit_style = """
            <style>
            #MainMenu {visibility: hidden;}
            footer {visibility: hidden;}
            </style>
            """
st.markdown(hide_streamlit_style, unsafe_allow_html=True)

model = load_model('model.h5')

st.title('Plant Disease Detection')
st.write("Just Upload your Plant's Leaf Image and get predictions if the plant is healthy or not")

uploaded_file = st.file_uploader("Choose an Image file", type=["png", "jpg"])

if uploaded_file is not None:
    progress = st.text("Crunching Image")
    my_bar = st.progress(0)
    image = Image.open(io.BytesIO(uploaded_file.read()))
    st.image(np.array(Image.fromarray(np.array(image)).resize((700, 400), Image.Resampling.LANCZOS)), width=None)
    my_bar.progress(40)
    image = clean_image(image)
    predictions, predictions_arr = get_prediction(model, image)
    my_bar.progress(70)
    result = make_results(predictions, predictions_arr)
    my_bar.progress(100)
    progress.empty()
    my_bar.empty()
    st.write(f"The plant {result['status']} with {result['prediction']} prediction.")
