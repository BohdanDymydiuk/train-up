import React, { useState } from 'react';

import { createPortal } from 'react-dom';
import { useNavigate } from 'react-router';

import { InputChangeEvent } from '../../../../types/Events';

import { Buttons } from './components/Buttons';
import { Inputs } from './components/Inputs';
import { SignUp } from './components/SignUp';
import { SignUpModal } from './components/SignUpModal';
import { Title } from './components/Title';

import styles from './Form.module.scss';

export const Form: React.FC = () => {
  const [isModalShown, setIsModalShown] = useState(false);
  const [emailName, setEmailName] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  // #region useEffects

  // useEffect(() => {
  //   if (isModalShown) {
  //     document.body.style.overflow = 'hidden';
  //   } else {
  //     document.body.style.overflow = 'auto';
  //   }
  // }, [isModalShown]);

  // #endregion
  // #region handlers

  const inputTextHandler = (event: InputChangeEvent) => {
    setEmailName(event.target.value);
  };

  const inputPasswordHandler = (event: InputChangeEvent) => {
    setPassword(event.target.value);
  };

  const onSubmitHandler = (event: React.FormEvent) => {
    event.preventDefault();

    navigate('/');
  };

  // #endregion
  // #region props

  const inputsProps = {
    emailName,
    password,
    inputTextHandler,
    inputPasswordHandler,
  };

  const signUpProps = { setIsModalShown };
  const modalProps = { setIsModalShown };

  // #endregion

  return (
    <div className={styles['form-wrapper']}>
      <Title />
      <form
        action='#'
        method='post'
        className={styles.form}
        onSubmit={onSubmitHandler}
      >
        <h3 className={styles['form-title']}>
          Welcome to your creative space!
        </h3>
        <Inputs {...inputsProps} />
        <Buttons />
      </form>
      {isModalShown &&
        createPortal(<SignUpModal {...modalProps} />, document.body)}
      <SignUp {...signUpProps} />
    </div>
  );
};
