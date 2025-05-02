import React from "react";
import {
  FormControl,
  InputLabel,
  OutlinedInput,
  InputAdornment,
  IconButton,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";

export const PasswordField = () => {
  const [showPassword, setShowPassword] = React.useState(false);

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  return (
    <FormControl>
      <InputLabel htmlFor="password">Password</InputLabel>
      <OutlinedInput
        id="password"
        type={showPassword ? "text" : "password"}
        required
        endAdornment={
          <InputAdornment position="end">
            <IconButton
              aria-label={showPassword ? "Esconder a senha" : "Mostrar a senha"}
              onClick={handleClickShowPassword}
              edge="end"
            >
              {showPassword ? <VisibilityOff /> : <Visibility />}
            </IconButton>
          </InputAdornment>
        }
        label="Password"
      />
    </FormControl>
  );
};
